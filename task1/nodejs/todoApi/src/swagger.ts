import swaggerJSDoc from 'swagger-jsdoc';

export const swaggerSpec = swaggerJSDoc({
    definition: {
        openapi: '3.0.0',
        info: {
            title: 'Todo List API',
            version: '1.0.0',
            description: 'A simple REST API to manage Todo tasks',
        },
    },
    apis: ['./src/routes/*.ts', './src/models/*.ts'], // 扫描这些文件中的注释
});
