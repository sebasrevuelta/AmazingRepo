// ============================================================
// BOOTSTRAP HOUSEKEEPER USER - POSTGRESQL
// ============================================================

/**
 * POST /postgres/DbHousekeeperUser
 * Connects to a newly created PostgreSQL cluster using root credentials and creates user.
 * Used for bootstrapping new clusters where user does not yet exist.
 *
 * Request body:
 *   - host: DB cluster endpoint
 *   - port: DB port (default: 5432)
 *   - rootUsername: master username 
 *   - rootPassword: master password from Secrets Manager
 */
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

    const pool = new Pool({
        host,
        port:                     parseInt(port),
        user:                     rootUsername,
        password:                 rootPassword,
        database:                 'postgres',
        connectionTimeoutMillis:  30000,
    });

    let client;
    try {
        client = await pool.connect();
        logToFile('INFO', requestId, `Connected to PostgreSQL at ${host}:${port}`);

        // Check if user already exists
