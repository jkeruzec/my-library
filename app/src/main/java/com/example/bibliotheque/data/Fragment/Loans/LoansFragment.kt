package com.example.bibliotheque.data.Fragment.Loans

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.bibliotheque.R
import com.example.bibliotheque.data.Fragment.Loan.LoanFragment

class LoansFragment : Fragment() {

    companion object {
        fun newInstance() = LoansFragment()
    }

    private lateinit var viewModel: LoansViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loans_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var loanFragment : LoanFragment = LoanFragment()
        var loanFragment2 : LoanFragment = LoanFragment()
        childFragmentManager.beginTransaction().add(R.id.loansFragmentContainer, loanFragment).add(R.id.loansFragmentContainer, loanFragment2).commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoansViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
