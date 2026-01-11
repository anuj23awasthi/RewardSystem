const Reward = require("../models/Reward");
const Redemption = require("../models/Redemption");
const User = require("../models/User");

// GET all rewards (safe version, hide couponCode)
exports.getRewards = async (req, res) => {
    try {
        const rewards = await Reward.find({}, "-couponCode");
        res.json(rewards);
    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: "Server error" });
    }
};

// POST redeem reward
exports.redeemReward = async (req, res) => {
    try {
        const { userId, rewardId } = req.body;
        const today = new Date().toLocaleDateString("en-CA"); // gives YYYY-MM-DD in local timezone

        const user = await User.findById(userId);
        const reward = await Reward.findById(rewardId);

        if (!user || !reward) {
            return res.status(400).json({ msg: "Invalid user or reward" });
        }

        // Not enough tokens
        if (user.tokens < reward.cost) {
            return res.status(400).json({ msg: "Not enough tokens" });
        }

        // Deduct cost
        user.tokens -= reward.cost;
        await user.save();

        // Save redemption
        const redemption = new Redemption({
            user: user._id,
            reward: reward._id,
            date: today,
            couponCode: reward.couponCode
        });

        await redemption.save();

        res.json({
            msg: "Reward redeemed successfully",
            couponCode: reward.couponCode,
            remainingTokens: user.tokens
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: "Server error" });
    }
};
