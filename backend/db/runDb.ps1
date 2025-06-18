docker run --name pg `
-e POSTGRES_USER=user `
-e POSTGRES_PASSWORD=pass `
-e POSTGRES_DB=grafosupporterDB `
-p 127.0.0.1:5432:5432 `
-d postgres:16
