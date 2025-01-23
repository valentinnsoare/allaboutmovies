db = db.getSiblingDB('allaboutmovies');

db.createUser({
  user: "admin",
  pwd: "admin", 
  roles: [
    { role: "root", db: "admin" } 
  ]
});
