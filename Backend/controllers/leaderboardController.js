const Leaderboard = require("../models/Leaderboard");
const User = require("../models/User");

// TODAY leaderboard
exports.getTodayLeaderboard = async (req, res) => {
    try {
        const today = new Date().toLocaleDateString("en-CA");  // LOCAL DATE YYYY-MM-DD

        const list = await Leaderboard.find({ date: today })
            .populate("user", "name email area city country tokens")
            .sort({ tokensEarned: -1 })
            .limit(50);

        res.json(list);

    } catch (error) {
        console.error("Leaderboard Error:", error);
        res.status(500).json({ msg: "Server error" });
    }
};

// GLOBAL leaderboard (all-time tokens)
exports.getGlobalLeaderboard = async (req, res) => {
    try {
        const list = await Leaderboard.aggregate([
            {
                $group: {
                    _id: "$user",                     // group by user
                    totalTokens: { $sum: "$tokensEarned" }
                }
            },
            {
                $sort: { totalTokens: -1 }
            },
            {
                $limit: 50
            }
        ]);

        // Populate user info in the grouped results
        const populated = await User.populate(list, {
            path: "_id",
            select: "name email area city country tokens"
        });

        res.json(populated);

    } catch (error) {
        console.error("Global Leaderboard Error:", error);
        res.status(500).json({ msg: "Server error" });
    }
};
