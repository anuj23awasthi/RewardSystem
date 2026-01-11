const Leaderboard = require("../models/Leaderboard");
const User = require("../models/User");

function fixFormat(item) {
    return {
        _id: item._id,                 // keep string id
        user: item.user,               // populated user
        tokensEarned: item.tokensEarned,
        correct: item.correct,
        submitted: item.submitted,
        date: item.date
    };
}

exports.getAreaLeaderboard = async (req, res) => {
    try {
        const { area } = req.params;
        const today = new Date().toLocaleDateString("en-CA");

        const list = await Leaderboard.find({ date: today })
            .populate("user", "name email area city country tokens")
            .sort({ tokensEarned: -1 });

        const filtered = list
            .filter(item => item.user.area?.toLowerCase() === area.toLowerCase())
            .map(fixFormat);  // 🔥 ensure JSON shape matches global

        res.json(filtered);

    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: "Server error" });
    }
};

exports.getCityLeaderboard = async (req, res) => {
    try {
        const { city } = req.params;
        const today = new Date().toLocaleDateString("en-CA");

        const list = await Leaderboard.find({ date: today })
            .populate("user", "name email area city country tokens")
            .sort({ tokensEarned: -1 });

        const filtered = list
            .filter(item => item.user.city?.toLowerCase() === city.toLowerCase())
            .map(fixFormat);

        res.json(filtered);

    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: "Server error" });
    }
};

exports.getCountryLeaderboard = async (req, res) => {
    try {
        const { country } = req.params;
        const today = new Date().toLocaleDateString("en-CA");

        const list = await Leaderboard.find({ date: today })
            .populate("user", "name email area city country tokens")
            .sort({ tokensEarned: -1 });

        const filtered = list
            .filter(item => item.user.country?.toLowerCase() === country.toLowerCase())
            .map(fixFormat);

        res.json(filtered);

    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: "Server error" });
    }
};
