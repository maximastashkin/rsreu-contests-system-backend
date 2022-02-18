db.createUser(
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
    )