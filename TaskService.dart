import 'dart:convert';
import 'package:http/http.dart' as http;
import 'ApiConfig.dart';
import 'Task.dart';

class TaskService {
  final _base = '${ApiConfig.baseUrl}/tasks';

  Future<List<Task>> fetchAll() async {
    final res = await http.get(Uri.parse(_base));
    if (res.statusCode == 200) {
      final list = jsonDecode(res.body) as List;
      return list.map((j) => Task.fromJson(j)).toList();
    }
    throw Exception('Failed to load tasks (${res.statusCode})');
  }

  Future<Task> create(Task t) async {
    final res = await http.post(Uri.parse(_base),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(t.toJson()));
    if (res.statusCode == 201) {
      return Task.fromJson(jsonDecode(res.body));
    }
    throw Exception('Create failed (${res.statusCode})');
  }

  Future<Task> update(Task t) async {
    final url = Uri.parse('$_base/${t.id}');
    final res = await http.put(url,
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(t.toJson()));
    if (res.statusCode == 200) {
      return Task.fromJson(jsonDecode(res.body));
    }
    throw Exception('Update failed (${res.statusCode})');
  }

  Future<void> delete(int id) async {
    final res = await http.delete(Uri.parse('$_base/$id'));
    if (res.statusCode != 204) {
      throw Exception('Delete failed (${res.statusCode})');
    }
  }
}