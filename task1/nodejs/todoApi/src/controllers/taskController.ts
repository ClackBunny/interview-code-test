// controllers/taskController.ts
import { Request, Response } from 'express';
import { taskService } from '../services/taskService';

const taskController = {
    getAll(req: Request, res: Response) {
        res.json(taskService.getAll());
    },

    getById(req: Request, res: Response) {
        const task = taskService.getById(req.params.id);
        task ? res.json(task) : res.status(404).send('Task not found');
    },

    getByDueDate(req: Request, res: Response) {
        res.json(taskService.getByDueDate(req.params.date));
    },

    add(req: Request, res: Response): void {
        const {title, description, dueDate, completed} = req.body;
        if (!title || !dueDate) {
            res.status(400).send('Missing title or dueDate');
            return;
        }
        const newTask = taskService.add({title, description, dueDate, completed: completed ?? false});
        res.status(201).json(newTask);
    },

    update(req: Request, res: Response) {
        const success = taskService.update(req.params.id, req.body);
        success ? res.send('Updated') : res.status(404).send('Task not found');
    },

    remove(req: Request, res: Response) {
        const success = taskService.remove(req.params.id);
        success ? res.send('Deleted') : res.status(404).send('Task not found');
    },
};

export default taskController; // ✅ 改成 default 导出
