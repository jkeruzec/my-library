package com.example.bibliotheque.data.Fragment.Loan

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bibliotheque.R

class LoanFragment : Fragment() {

    companion object {
        fun newInstance() = LoanFragment()
    }

    private lateinit var viewModel: LoanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoanViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
