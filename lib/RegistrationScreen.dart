import 'package:flutter/material.dart';

class RegistrationScreen extends StatefulWidget
{
  const RegistrationScreen({super.key});

  @override
  _RegistrationScreenState createState() => _RegistrationScreenState();
}

class _RegistrationScreenState extends State<RegistrationScreen>
{
  // Define controllers for each input field.
  final TextEditingController firstNameController = TextEditingController();
  final TextEditingController lastNameController  = TextEditingController();
  final TextEditingController emailController     = TextEditingController();
  final TextEditingController phoneController     = TextEditingController();
  final TextEditingController passwordController  = TextEditingController();
  final TextEditingController confirmPasswordController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  void _register()
  {
    // Validate form inputs and perform registration logic here.
    if (_formKey.currentState!.validate())
    {
      // For demo purposes, display a success message.
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Registration successful!')),
      );
      // In a real app, you would send this data to your API.
    }
  }

  @override
  Widget build(BuildContext context)
  {
    return Scaffold(
      appBar: AppBar(
        title: Text('biteTrack - Register'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              // First name field
              TextFormField(
                controller: firstNameController,
                decoration: InputDecoration(
                  labelText: 'First Name',
                  icon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty)
                  {
                    return 'Please enter your first name';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              // Last name field
              TextFormField(
                controller: lastNameController,
                decoration: InputDecoration(
                  labelText: 'Last Name',
                  icon: Icon(Icons.person_outline),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty)
                  {
                    return 'Please enter your last name';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              // Email field
              TextFormField(
                controller: emailController,
                decoration: InputDecoration(
                  labelText: 'Email',
                  icon: Icon(Icons.email),
                ),
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null ||
                      value.isEmpty ||
                      !RegExp(r'\S+@\S+\.\S+').hasMatch(value))
                  {
                    return 'Please enter a valid email address';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              // Phone field
              TextFormField(
                controller: phoneController,
                decoration: InputDecoration(
                  labelText: 'Phone',
                  icon: Icon(Icons.phone),
                ),
                keyboardType: TextInputType.phone,
                validator: (value) {
                  if (value == null || value.isEmpty)
                  {
                    return 'Please enter your phone number';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              // Password field
              TextFormField(
                controller: passwordController,
                decoration: InputDecoration(
                  labelText: 'Password',
                  icon: Icon(Icons.lock),
                ),
                obscureText: true,
                validator: (value) {
                  if (value == null || value.length < 6)
                  {
                    return 'Password must be at least 6 characters';
                  }
                  return null;
                },
              ),
              SizedBox(height: 16),
              // Confirm Password field
              TextFormField(
                controller: confirmPasswordController,
                decoration: InputDecoration(
                  labelText: 'Confirm Password',
                  icon: Icon(Icons.lock_outline),
                ),
                obscureText: true,
                validator: (value) {
                  if (value != passwordController.text)
                  {
                    return 'Passwords do not match';
                  }
                  return null;
                },
              ),
              SizedBox(height: 32),
              ElevatedButton(
                onPressed: _register,
                child: Text('Register'),
              ),
              SizedBox(height: 16),
              TextButton(
                onPressed: () {
                  // Navigate back to the login screen.
                  Navigator.pop(context);
                },
                child: Text('Already have an account? Login here.'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
