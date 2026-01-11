const mongoose = require("mongoose");
const User = require("./models/User");
const Leaderboard = require("./models/Leaderboard");
const Challenge = require("./models/Challenge");
require("dotenv").config();

(async () => {
    try {
        await mongoose.connect(process.env.MONGO_URI);
        console.log("Connected to MongoDB");

        // TODAY'S DATE
        const today = new Date().toISOString().split("T")[0];

        // Fetch today's challenge
        const challenge = await Challenge.findOne({ date: today });
        if (!challenge) {
            console.log("No challenge found for today. Run the app first.");
            process.exit();
        }

        console.log("Today's Question:", challenge.question);
        console.log("-------------------------------------");

        // Clear today's leaderboard
        await Leaderboard.deleteMany({ date: today });
        console.log("Cleared today's leaderboard");

        // Fetch all users
        const users = await User.find({});
        console.log(`Found ${users.length} users`);
        console.log("Simulating challenge answers...\n");

        for (let user of users) {

            // 70% chance user attempts challenge
            const willAttempt = Math.random() < 0.7;

            if (!willAttempt) {
                console.log(`${user.name} skipped the challenge`);
                continue;
            }

            // 50% chance of correct answer
            const correct = Math.random() < 0.5;

            if (correct) {
                user.tokens += 10;
                user.streak += 1;

                console.log(`${user.name} ✔ Correct (+10 tokens)`);

                // Update leaderboard
                let lb = await Leaderboard.findOne({ userId: user._id, date: today });

                if (!lb) {
                    lb = new Leaderboard({
                        userId: user._id,
                        tokensEarned: 10,
                        date: today
                    });
                } else {
                    lb.tokensEarned += 10;
                }

                await lb.save();

            } else {
                console.log(`${user.name} ❌ Wrong (0 tokens)`);
                user.streak = 0;
            }

            await user.save();
        }

        console.log("\n✨ Leaderboard simulation complete!");
        console.log("Check leaderboard using:");
        console.log("http://localhost:5000/leaderboard/today");

        mongoose.connection.close();

    } catch (error) {
        console.error("Seeder Error:", error);
        mongoose.connection.close();
    }
})();
