import { Task } from '../models/Task';
import fs from 'fs-extra';
import path from 'path';

const filePath = path.join(__dirname, '../data/tasks.json');

function read(): Task[] {
    return fs.readJsonSync(filePath, { throws: false }) || [];
}

function write(data: Task[]) {
    fs.writeJsonSync(filePath, data, { spaces: 2 });
}

function generateId(): string {
    return Math.random().toString(36).slice(2, 10);
}

export const taskService = {
    getAll(): Task[] {
        return read();
    },

    getById(id: string): Task | undefined {
        return read().find(t => t.id === id);
    },

    getByDueDate(date: string): Task[] {
        return read().filter(t => t.dueDate === date);
    },

    add(task: Omit<Task, 'id' | 'createTime'>): Task {
        const newTask: Task = {
            ...task,
            id: generateId(),
            createTime: new Date().toISOString(),
        };
        const list = read();
        list.push(newTask);
        write(list);
        return newTask;
    },

    update(id: string, updated: Partial<Task>): boolean {
        const list = read();
        const idx = list.findIndex(t => t.id === id);
        if (idx === -1) return false;
        list[idx] = {
            ...list[idx],
            ...updated,
            lastUpdateTime: new Date().toISOString(),
        };
        write(list);
        return true;
    },

    remove(id: string): boolean {
        const list = read();
        const newList = list.filter(t => t.id !== id);
        if (newList.length === list.length) return false;
        write(newList);
        return true;
    }
};
