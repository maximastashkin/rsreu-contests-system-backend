admin = db.getSiblingDB("admin");
admin.createUser(
    {
        user: "root",
        pwd: "root",
        roles: [ { role: "root", db: "admin" } ]
    }
);
admin.auth("root", "root");
rcs = db.getSiblingDB("rcs-db");
rcs.createUser(
    {
        user: "mongo-user",
        pwd: "1234",
        roles: [
            {
                role: "readWrite",
                db: "rcs-db"
            }
        ]
    }
);