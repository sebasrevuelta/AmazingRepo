            `SELECT usename FROM pg_catalog.pg_user WHERE usename = $1`,
            [HOUSEKEEPER_USER]
        );

        if (checkResult.rows.length > 0) {
            logToFile('INFO', requestId, `User ${HOUSEKEEPER_USER} already exists, skipping`);
            client.release();
            await pool.end();
            return res.status(200).json({ error: false, message: `${HOUSEKEEPER_USER} already exists` });
        }

        // Create user
        await client.query(`CREATE USER ${client.escapeIdentifier(HOUSEKEEPER_USER)} WITH PASSWORD ${client.escapeLiteral(HOUSEKEEPER_PASSWORD)}`);
        logToFile('INFO', requestId, `User ${HOUSEKEEPER_USER} created`);

        // Grant roles
        await client.query(`GRANT rds_superuser TO ${client.escapeIdentifier(HOUSEKEEPER_USER)}`);
        await client.query(`GRANT ALL PRIVILEGES ON SCHEMA public TO ${client.escapeIdentifier(HOUSEKEEPER_USER)}`);
        await client.query(`GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ${client.escapeIdentifier(HOUSEKEEPER_USER)}`);
        await client.query(`ALTER ROLE ${client.escapeIdentifier(HOUSEKEEPER_USER)} CREATEDB`);
        logToFile('INFO', requestId, `Granted rds_superuser, schema privileges and CREATEDB to ${HOUSEKEEPER_USER}`);

        client.release();
        await pool.end();

        const elapsed = Date.now() - startTime;
        logResponse(requestId, 200, elapsed, true);
        logger.info(`[CELL-AGENT] [${requestId}] postgres/DbHousekeeperUser SUCCESS in ${elapsed}ms`);

        return res.status(200).json({ error: false, message: `${HOUSEKEEPER_USER} created and granted successfully` });

    } catch (error) {
        if (client) client.release();
        await pool.end();
        const elapsed = Date.now() - startTime;
        logToFile('ERROR', requestId, `Error: ${error.message}`);
        logResponse(requestId, 500, elapsed, false, error.message);
        return res.status(500).json({ error: true, message: error.message });
    }
});

module.exports = server;
