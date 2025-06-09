import { RequestHandler } from 'express';
import basicAuth from 'basic-auth';

const USERNAME = 'admin';
const PASSWORD = 'admin';

export const auth: RequestHandler = (req, res, next) => {
    const user = basicAuth(req);
    if (!user || user.name !== USERNAME || user.pass !== PASSWORD) {
        res.set('WWW-Authenticate', 'Basic realm="Todo API"');
        res.status(401).send('Authentication required.');
        return;
    }
    next();
}
