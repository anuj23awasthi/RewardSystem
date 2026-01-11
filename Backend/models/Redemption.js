const mongoose = require("mongoose");

const RedemptionSchema = new mongoose.Schema({
    user: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
    reward: { type: mongoose.Schema.Types.ObjectId, ref: "Reward", required: true },
    date: { type: String, required: true },
    couponCode: { type: String, required: true }
});

module.exports = mongoose.model("Redemption", RedemptionSchema);
