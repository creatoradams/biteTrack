class Task
{
  final int? id; // id is null for new objects
  final String title;
  final bool done;

  Task({this.id, required this.title, required this.done});

  factory Task.fromJson(Map<String, dynamic> j) =>
      Task(id: j['id'], title: j['title'], done: j['done'] as bool);

  Map<String, dynamic> toJson() =>
      {'id': id, 'title': title, 'done': done};
}