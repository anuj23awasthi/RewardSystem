const mongoose = require("mongoose");
const User = require("./models/User");
require("dotenv").config();

mongoose.connect(process.env.MONGO_URI)
  .then(async () => {
    console.log("Connected to MongoDB");

    // Remove all old users
    await User.deleteMany({});
    console.log("Old users removed");

    const userLocations = [
      { area: "Baner", city: "Pune", country: "India" },
      { area: "Baner", city: "Pune", country: "India" },
      { area: "Andheri", city: "Mumbai", country: "India" },
      { area: "Andheri", city: "Mumbai", country: "India" },
      { area: "Whitefield", city: "Bangalore", country: "India" },
      { area: "Whitefield", city: "Bangalore", country: "India" },
      { area: "Salt Lake", city: "Kolkata", country: "India" },
      { area: "Salt Lake", city: "Kolkata", country: "India" },
      { area: "Connaught Place", city: "Delhi", country: "India" },
      { area: "Connaught Place", city: "Delhi", country: "India" }
    ];

    const users = [];

    for (let i = 1; i <= 10; i++) {
      users.push({
        name: `Test User ${i}`,
        email: `test${i}@example.com`,
        password: "$2b$10$3Da6vYO5A7ZPj7M0YRdJ7eJFzO8z6wB2LQm4R4L7Twr.pEksXBZ2S",
        tokens: 0,
        streak: 0,
        area: userLocations[i - 1].area,
        city: userLocations[i - 1].city,
        country: userLocations[i - 1].country
      });
    }

    await User.insertMany(users);

    console.log("✨ 10 test users inserted successfully with location data!");
    mongoose.connection.close();
  })
  .catch(err => console.error(err));
