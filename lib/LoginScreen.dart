import 'package:flutter/material.dart';

class LoginScreen extends StatelessWidget
{
  // Controllers for capturing user input
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  LoginScreen({super.key});

  void _login(BuildContext context)
  {
    // Placeholder for login logic
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text("Login functionality not implemented.")),
    );
  }

  @override
  Widget build(BuildContext context)
  {
    return Scaffold(
      // Removing the AppBar for a custom design where the header and logo are centered.
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Center(
          // SingleChildScrollView enables scrolling if content doesn't fit on smaller screens.
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // Logo Image: Ensure the path matches the registration in pubspec.yaml
                Image.asset(
                  'Assets/logo.jpg',
                  height: 120, // Adjust the height as needed
                ),
                SizedBox(height: 24),
                // Header text, centered, and with larger size.
                Text(
                  'Login',
                  style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold),
                  textAlign: TextAlign.center,
                ),
                SizedBox(height: 32),
                // Email Field
                TextField(
                  controller: emailController,
                  style: TextStyle(fontSize: 18),
                  decoration: InputDecoration(
                    labelText: 'Email',
                    hintText: 'Enter your email',
                    icon: Icon(Icons.email, size: 28),
                  ),
                  keyboardType: TextInputType.emailAddress,
                ),
                SizedBox(height: 24),
                // Password Field
                TextField(
                  controller: passwordController,
                  style: TextStyle(fontSize: 18),
                  decoration: InputDecoration(
                    labelText: 'Password',
                    hintText: 'Enter your password',
                    icon: Icon(Icons.lock, size: 28),
                  ),
                  obscureText: true,
                ),
                SizedBox(height: 32),
                // Login Button with larger text.
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    padding: EdgeInsets.symmetric(horizontal: 32, vertical: 16),
                    textStyle: TextStyle(fontSize: 20),
                  ),
                  onPressed: () => _login(context),
                  child: Text('Login'),
                ),
                SizedBox(height: 20),
                // Link to registration screen.
                TextButton(
                  onPressed: () {
                    Navigator.pushNamed(context, '/register');
                  },
                  style: TextButton.styleFrom(
                    textStyle: TextStyle(fontSize: 18),
                  ),
                  child: Text("Don't have an account? Register here."),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
