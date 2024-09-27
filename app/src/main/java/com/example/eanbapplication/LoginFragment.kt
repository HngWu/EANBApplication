package com.example.eanbapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.eanbapplication.Models.User
import com.example.eanbapplication.Ultilities.httploginservice
import com.example.eanbapplication.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Login"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Login"


        binding.buttonLogin.setOnClickListener { view ->
            val newStudentObj = User(binding.editTextUsername.text.toString(),binding.editTextPassword.text.toString())

            httploginservice(this,newStudentObj).execute()
            Log.d("Logging", "LoginFragment")

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loginSuccess() {
        Log.d("Logging", "LoginFragment")


        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    fun loginfailed() {
        Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
    }


}