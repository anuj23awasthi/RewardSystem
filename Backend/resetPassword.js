const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");
const User = require("./models/User");
require("dotenv").config();

mongoose.connect(process.env.MONGO_URI).then(async () => {
  console.log("Connected to MongoDB");

  const email = "test1@example.com";   // change if needed
  const newPassword = "1234";          // the new password you want

  const hashed = await bcrypt.hash(newPassword, 10);

  await User.updateOne(
    { email },
    { $set: { password: hashed } }
  );

  console.log("Password reset to:", newPassword);
  mongoose.connection.close();
});
