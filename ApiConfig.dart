import 'package:flutter/foundation.dart' show kIsWeb;

class ApiConfig
{
  // change this ONE spot when deploy
  static const _port = '3434'; // Spring Boot's server.port
  static String get host
  {
    // Browser hits localhost directly; Android emulator needs 10.0.2.2
    return kIsWeb ? 'localhost' : '10.0.2.2';
  }

  static String get baseUrl => 'http://$host:$_port/api';
}