#!/usr/bin/env bash
echo 'init mongo user ...'
mongo 127.0.0.1/car_bot_db --eval "
    db.createUser({
        user: \"car_bot\",
        pwd: \"car_bot\",
        roles: [{
            role: \"readWrite\",
            db: \"car_bot_db\"
        }]
    });
";
echo 'init mongo user done ...'
