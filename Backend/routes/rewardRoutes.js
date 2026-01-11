const express = require("express");
const router = express.Router();
const { getRewards, redeemReward } = require("../controllers/rewardController");

router.get("/", getRewards);
router.post("/redeem", redeemReward);

module.exports = router;
