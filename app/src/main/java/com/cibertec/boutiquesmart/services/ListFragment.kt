package com.cibertec.boutiquesmart.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.databinding.FragmentListBinding
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.repository.ProductRepository

class ListFragment : Fragment() {
    private lateinit var db: DBHelper
    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: RecyclerAdapterShop
    private var listener: (Product) -> Unit = {}
    lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DBHelper(requireContext())
        cartRepository = CartRepository(db)
        productRepository = ProductRepository(db)
        setUpRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setListener(l: (Product) -> Unit) {
        listener = l
    }

    private fun setUpRecyclerView() {
        binding.recyclerProducts.setHasFixedSize(true)
        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = RecyclerAdapterShop(requireActivity(), getProducts(), user.id)
        binding.recyclerProducts.adapter = mAdapter
    }

    private fun getProducts(): MutableList<Product> {
        return if (user != null)
            cartRepository.getCartByUserId(user.id).toMutableList()
        else
            productRepository.getAllProducts().toMutableList()
    }
}