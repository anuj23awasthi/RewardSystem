const mongoose = require("mongoose");

const RewardSchema = new mongoose.Schema({
    title: { type: String, required: true },
    description: { type: String, default: "" },
    cost: { type: Number, required: true }, // token cost
    brand: { type: String, required: true },
    couponCode: { type: String, required: true }, // or generate dynamically
    expiryDate: { type: String, default: "" }
});

module.exports = mongoose.model("Reward", RewardSchema);
