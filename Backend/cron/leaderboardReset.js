const cron = require("node-cron");
const Leaderboard = require("../models/Leaderboard");
const User = require("../models/User");

const resetDailyLeaderboard = () => {

    // Runs every day at midnight
    cron.schedule("0 0 * * *", async () => {
        try {
            console.log("🏆 Checking top 3 leaderboard users...");

            const today = new Date().toLocaleDateString("en-CA"); // gives YYYY-MM-DD in local timezone

            // Fetch today leaderboard sorted by highest tokens
            const topUsers = await Leaderboard.find({ date: today })
                .sort({ tokensEarned: -1 })
                .limit(3)
                .populate("user");

            // Award bonuses
            if (topUsers[0]) {
                topUsers[0].user.tokens += 50;
                await topUsers[0].user.save();
                console.log("🥇 Gave +50 tokens to:", topUsers[0].user.name);
            }

            if (topUsers[1]) {
                topUsers[1].user.tokens += 30;
                await topUsers[1].user.save();
                console.log("🥈 Gave +30 tokens to:", topUsers[1].user.name);
            }

            if (topUsers[2]) {
                topUsers[2].user.tokens += 20;
                await topUsers[2].user.save();
                console.log("🥉 Gave +20 tokens to:", topUsers[2].user.name);
            }

            console.log("💥 Resetting today's leaderboard...");
            await Leaderboard.deleteMany({ date: today });

            console.log("✅ Leaderboard reset successfully");

        } catch (error) {
            console.error("❌ Leaderboard bonus/reset error:", error);
        }
    });
};

module.exports = resetDailyLeaderboard;
