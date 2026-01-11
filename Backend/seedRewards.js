const mongoose = require("mongoose");
const Reward = require("./models/Reward");
require("dotenv").config();

mongoose.connect(process.env.MONGO_URI)
  .then(async () => {
    console.log("Connected to MongoDB");

    // Clear old rewards
    await Reward.deleteMany({});
    console.log("Old rewards removed");

    // Reward list
    const rewards = [
      {
        title: "Amazon ₹20 Gift Voucher",
        description: "Redeem on any product on Amazon India.",
        brand: "Amazon",
        cost: 200,
        couponCode: "AMZ20-" + Math.random().toString(36).substring(2, 8),
        expiryDate: "2026-12-31"
      },
      {
        title: "PayTM ₹10 Cash",
        description: "Instant cashback added to your PayTM wallet.",
        brand: "PayTM",
        cost: 100,
        couponCode: "PAY10-" + Math.random().toString(36).substring(2, 8),
        expiryDate: "2026-12-31"
      },
      {
        title: "Flipkart ₹30 Voucher",
        description: "Valid on selected Flipkart products.",
        brand: "Flipkart",
        cost: 300,
        couponCode: "FLIP30-" + Math.random().toString(36).substring(2, 8),
        expiryDate: "2026-12-31"
      },
      {
        title: "Myntra ₹50 Fashion Discount",
        description: "Applicable on clothing and accessories.",
        brand: "Myntra",
        cost: 500,
        couponCode: "MYN50-" + Math.random().toString(36).substring(2, 8),
        expiryDate: "2026-12-31"
      },
      {
        title: "Zomato ₹25 Food Coupon",
        description: "Enjoy a discount on food orders.",
        brand: "Zomato",
        cost: 250,
        couponCode: "ZMT25-" + Math.random().toString(36).substring(2, 8),
        expiryDate: "2026-12-31"
      }
    ];

    // Insert into DB
    await Reward.insertMany(rewards);

    console.log("✨ Rewards inserted successfully!");
    mongoose.connection.close();
  })
  .catch(err => console.error(err));
