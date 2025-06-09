import { Router } from 'express';
import taskController from '../controllers/taskController';

const router = Router();

/**
 * @swagger
 * components:
 *   schemas:
 *     Task:
 *       type: object
 *       required:
 *         - title
 *         - dueDate
 *       properties:
 *         id:
 *           type: string
 *         title:
 *           type: string
 *         description:
 *           type: string
 *         dueDate:
 *           type: string
 *           format: date
 *         completed:
 *           type: boolean
 */

/**
 * @swagger
 * /tasks:
 *   get:
 *     summary: 获取所有任务
 *     responses:
 *       200:
 *         description: 返回任务列表
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Task'
 */
router.get('/', taskController.getAll);
/**
 * @swagger
 * /tasks/{id}:
 *   get:
 *     summary: 获取指定 ID 的任务
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: 返回任务详情
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Task'
 *       404:
 *         description: 未找到任务
 */
router.get('/:id', taskController.getById);
/**
 * @swagger
 * /tasks/due/{date}:
 *   get:
 *     summary: 获取指定日期的任务
 *     parameters:
 *       - in: path
 *         name: date
 *         required: true
 *         schema:
 *           type: string
 *           format: date
 *     responses:
 *       200:
 *         description: 返回指定日期的任务列表
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Task'
 *       400:
 *         description: 缺少或无效的日期参数
 */
router.get('/due/:date', taskController.getByDueDate);
/**
 * @swagger
 * /tasks:
 *   post:
 *     summary: 创建新任务
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Task'
 *     responses:
 *       201:
 *         description: 创建成功，返回创建的任务
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Task'
 *       400:
 *         description: 请求参数不完整或格式错误
 */
router.post('/', taskController.add);
/**
 * @swagger
 * /tasks/{id}:
 *   put:
 *     summary: 修改指定任务
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/Task'
 *     responses:
 *       200:
 *         description: 修改成功
 *       404:
 *         description: 未找到任务
 */
router.put('/:id', taskController.update);
/**
 * @swagger
 * /tasks/{id}:
 *   delete:
 *     summary: 删除指定任务
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: 删除成功
 *       404:
 *         description: 未找到任务
 */
router.delete('/:id', taskController.remove);

export default router;
