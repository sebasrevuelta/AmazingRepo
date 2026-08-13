server.post('/postgres/DbHousekeeperUser', authenticateToken, async (req, res) => {
    const requestId = Date.now().toString(36) + Math.random().toString(36).substr(2, 5);
    const startTime = Date.now();

    logSeparator(requestId, 'POSTGRESQL BOOTSTRAP HOUSEKEEPER USER');

    const { host, port = 5432, rootUsername, rootPassword } = req.body;

    logToFile('INFO', requestId, `Host: ${host}:${port}`);
    logToFile('INFO', requestId, `Root user: ${rootUsername}`);

    if (!host)         return res.status(400).json({ error: true, message: 'Missing required parameter: host' });
    if (!rootUsername) return res.status(400).json({ error: true, message: 'Missing required parameter: rootUsername' });
    if (!rootPassword) return res.status(400).json({ error: true, message: 'Missing required parameter: rootPassword' });

    const HOUSEKEEPER_USER     = process.env.GANDALF_DB_USER;
    const HOUSEKEEPER_PASSWORD = process.env.GANDALF_DB_PASSWORD;

    if (!HOUSEKEEPER_USER || !HOUSEKEEPER_PASSWORD) {
        return res.status(500).json({ error: true, message: 'GANDALF_DB_USER or GANDALF_DB_PASSWORD env vars not set' });
    }

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
