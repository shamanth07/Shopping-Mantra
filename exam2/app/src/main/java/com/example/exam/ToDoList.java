package com.example.exam;

public class ToDoList {
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<String> taskList;

        public TaskAdapter(List<String> taskList) {
            this.taskList = taskList;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            String task = taskList.get(position);
            holder.taskTextView.setText(task);
            holder.deleteButton.setOnClickListener(v -> {
                taskList.remove(position);
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        static class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView taskTextView;
            Button deleteButton;

            TaskViewHolder(View itemView) {
                super(itemView);
                taskTextView = itemView.findViewById(R.id.task_text);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }


}
