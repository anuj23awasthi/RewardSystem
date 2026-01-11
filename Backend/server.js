const express = require("express");
require("dotenv").config();
const cors = require("cors");
const connectDB = require("./config/db");

// Routes
const authRoutes = require("./routes/authRoutes");
const challengeRoutes = require("./routes/challengeRoutes");
const leaderboardRoutes = require("./routes/leaderboardRoutes");
const locationLeaderboardRoutes = require("./routes/locationLeaderboardRoutes"); // ✅ FIXED (import added)
const rewardRoutes = require("./routes/rewardRoutes");

// Cron – resets daily leaderboard
require("./cron/leaderboardReset");

const Challenge = require("./models/Challenge");

const app = express();
app.use(cors());
app.use(express.json());

// Connect to MongoDB
connectDB();

// DAILY CHALLENGE RESET
async function resetChallengeDaily() {
  const today = new Date().toLocaleDateString("en-CA"); // YYYY-MM-DD (Local)
  await Challenge.deleteMany({ date: { $ne: today } });
  console.log("✓ Old challenges cleared");
}
resetChallengeDaily();

// ROUTES
app.use("/auth", authRoutes);
app.use("/challenge", challengeRoutes);

app.use("/leaderboard", leaderboardRoutes);          // today + global
app.use("/leaderboard", locationLeaderboardRoutes);  // area + city + country

app.use("/rewards", rewardRoutes);

// HEALTH CHECK
app.get("/", (req, res) => {
  res.send("Sharp Rewards Backend Running 🚀");
});

// START SERVER
const PORT = process.env.PORT || 5000;
app.listen(PORT, "0.0.0.0", () => {
  console.log(`Server running on port ${PORT}`);
});
