export interface Task {
    id: string;
    title: string;
    description?: string;
    dueDate: string; // yyyy-MM-dd
    completed: boolean;
    createTime: string;
    lastUpdateTime?: string;
}
