const Challenge = require("../models/Challenge");
const User = require("../models/User");
const Leaderboard = require("../models/Leaderboard");
const { generateDailyQuestion } = require("../utils/questionGenerator");

// GET today's challenge
exports.getTodayChallenge = async (req, res) => {
    try {
        console.log("SERVER LOCAL DATE:", new Date().toLocaleDateString("en-CA"));
console.log("SERVER UTC DATE:", new Date().toISOString().split("T")[0]);

        const today = new Date().toLocaleDateString("en-CA"); // gives YYYY-MM-DD in local timezone

        let challenge = await Challenge.findOne({ date: today });

        if (!challenge) {
            const q = generateDailyQuestion();
            challenge = new Challenge({
                question: q.question,
                correctAnswer: q.correctAnswer,
                date: today
            });
            await challenge.save();
        }

        res.json({
            question: challenge.question,
            date: challenge.date
        });

    } catch (error) {
        console.error("Challenge Error:", error);
        res.status(500).json({ msg: "Server error" });
    }
};

// POST submit challenge
exports.submitChallenge = async (req, res) => {
    try {
        const { userId, answer } = req.body;
        const today = new Date().toLocaleDateString("en-CA"); // gives YYYY-MM-DD in local timezone

        const challenge = await Challenge.findOne({ date: today });
        if (!challenge) return res.status(400).json({ msg: "Challenge not found" });

        const user = await User.findById(userId);
        if (!user) return res.status(400).json({ msg: "User not found" });

        // ❗ Prevent multiple submissions per day
        const existingEntry = await Leaderboard.findOne({ user: user._id, date: today });
        if (existingEntry && existingEntry.submitted) {
            return res.json({
                msg: "Already submitted today",
                correct: existingEntry.correct,
                tokensEarned: existingEntry.tokensEarned,
                newStreak: user.streak
            });
        }

        let correct = false;
        let tokensEarned = 0;

        if (answer == challenge.correctAnswer) {
            correct = true;
            tokensEarned = 10;

            user.streak += 1;
            user.tokens += tokensEarned;
            await user.save();

            let lb = await Leaderboard.findOne({ user: user._id, date: today });

            if (!lb) {
                lb = new Leaderboard({
                    user: user._id,
                    tokensEarned,
                    correct: true,
                    submitted: true,
                    date: today
                });
            } else {
                lb.tokensEarned += tokensEarned;
                lb.correct = true;
                lb.submitted = true;
            }

            await lb.save();

        } else {
            correct = false;
            tokensEarned = 0;

            user.streak = 0;
            await user.save();

            await Leaderboard.updateOne(
                { user: user._id, date: today },
                {
                    user: user._id,
                    tokensEarned: 0,
                    correct: false,
                    submitted: true,
                    date: today
                },
                { upsert: true }
            );
        }

        res.json({
            correct,
            tokensEarned,
            newStreak: user.streak
        });

    } catch (error) {
        console.error("Submit Error:", error);
        res.status(500).json({ msg: "Server error" });
    }
};
