package com.thecodecup.ui.details.details_fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.thecodecup.R
import Coffee
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thecodecup.data.local.AppDatabase
import com.thecodecup.data.model.CartItemEntity
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    private var selectedShot = "single"
    private var selectedType = "iced"
    private var selectedSize = "medium"
    private var selectedIce = "normal"

    private var coffee: Coffee? = null
    private var quantity = 1

    private lateinit var txtQuantity: TextView
    private lateinit var txtTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coffee = arguments?.getParcelable("coffee")

        val imgCoffee = view.findViewById<ImageView>(R.id.coffeeImage)
        val txtName = view.findViewById<TextView>(R.id.txtCoffeeName)
        txtQuantity = view.findViewById(R.id.txtQuantity)
        txtTotal = view.findViewById(R.id.txtTotalAmount)

        val btnPlus = view.findViewById<Button>(R.id.btnIncrease)
        val btnMinus = view.findViewById<Button>(R.id.btnDecrease)
        val btnAddToCart = view.findViewById<Button>(R.id.btnAddToCart)

        val radioSingle = view.findViewById<RadioButton>(R.id.radioSingle)
        val radioDouble = view.findViewById<RadioButton>(R.id.radioDouble)

        val iconHot = view.findViewById<ImageView>(R.id.iconHot)
        val iconCold = view.findViewById<ImageView>(R.id.iconCold)

        val sizeSmall = view.findViewById<ImageView>(R.id.sizeSmall)
        val sizeMedium = view.findViewById<ImageView>(R.id.sizeMedium)
        val sizeLarge = view.findViewById<ImageView>(R.id.sizeLarge)

        val iceNone = view.findViewById<ImageView>(R.id.iceNone)
        val iceNormal = view.findViewById<ImageView>(R.id.iceNormal)
        val iceFull = view.findViewById<ImageView>(R.id.iceFull)

        val btnCart = view.findViewById<ImageButton>(R.id.btnCart)
        btnCart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }



        coffee?.let {
            imgCoffee.setImageResource(it.imageResId)
            txtName.text = it.name
        }

        fun updateTotal() {
            val total = (coffee?.price ?: 0.0) * quantity
            txtQuantity.text = quantity.toString()
            txtTotal.text = String.format("$%.2f", total)
        }

        btnPlus.setOnClickListener {
            quantity++
            updateTotal()
        }

        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateTotal()
            }
        }

        btnAddToCart.setOnClickListener {
            // Thêm item vào database
            lifecycleScope.launch {
                val profileDao = AppDatabase.getInstance(requireContext()).userProfileDao()
                val profile = profileDao.getProfile()

                if (profile == null || profile.name.isBlank() || profile.phone.isBlank() || profile.address.isBlank() || profile.email.isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Vui lòng điền đầy đủ thông tin cá nhân trước khi đặt hàng.",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.profileFragment)
                    return@launch
                }

                // Build entity từ đối tượng Coffee và các lựa chọn của user
                val item = CartItemEntity(
                    name      = coffee!!.name,
                    imageResId= coffee!!.imageResId,
                    shot      = selectedShot,
                    type      = selectedType,
                    size      = selectedSize,
                    ice       = selectedIce,
                    quantity  = quantity,
                    price     = coffee!!.price,
                    address = profile.address
                )
                // Ghi vào Room
                AppDatabase
                    .getInstance(requireContext())
                    .cartDao()
                    .insert(item)

                // Sau khi ghi xong, chuyển sang CartFragment
                findNavController().navigate(R.id.cartFragment)
            }
        }

// === Shot logic ===
        radioSingle.setOnClickListener {
            // Set selected style
            selectedShot = "single"
            radioSingle.setTextColor(Color.WHITE)
            radioSingle.setBackgroundResource(R.drawable.bg_segment_btn_blue)

            // Reset unselected
            radioDouble.setTextColor(Color.parseColor("#FF000000"))
            radioDouble.setBackgroundResource(R.drawable.bg_segment_button) // Remove background
        }

        radioDouble.setOnClickListener {
            // Set selected style
            selectedShot = "double"
            radioDouble.setTextColor(Color.WHITE)
            radioDouble.setBackgroundResource(R.drawable.bg_segment_btn_blue)

            // Reset unselected
            radioSingle.setTextColor(Color.parseColor("#FF000000"))
            radioSingle.setBackgroundResource(R.drawable.bg_segment_button) // Remove background
        }


        // === Hot/Cold logic ===
        fun updateHotCold(selected: ImageView, others: List<ImageView>) {

            selected.setColorFilter(Color.BLACK)
            others.forEach { it.setColorFilter(Color.parseColor("#D8D8D8")) }
        }

        iconHot.setOnClickListener {
            selectedType = "hot"
            updateHotCold(iconHot, listOf(iconCold))
        }

        iconCold.setOnClickListener {
            selectedType = "cold"
            updateHotCold(iconCold, listOf(iconHot))
        }

        // === Size logic ===
        fun updateSize(selected: ImageView, others: List<ImageView>) {
            selected.setColorFilter(Color.BLACK)
            others.forEach { it.setColorFilter(Color.parseColor("#D8D8D8")) }
        }

        sizeSmall.setOnClickListener {
            selectedSize = "small"
            updateSize(sizeSmall, listOf(sizeMedium, sizeLarge))
        }

        sizeMedium.setOnClickListener {
            selectedSize = "medium"
            updateSize(sizeMedium, listOf(sizeSmall, sizeLarge))
        }

        sizeLarge.setOnClickListener {
            selectedSize = "large"
            updateSize(sizeLarge, listOf(sizeSmall, sizeMedium))
        }

        // === Ice logic ===
        fun updateIce(selected: ImageView, others: List<ImageView>) {
            selected.setColorFilter(Color.BLACK)
            others.forEach { it.setColorFilter(Color.parseColor("#D8D8D8")) }
        }

        iceNone.setOnClickListener {
            selectedIce = "none"
            updateIce(iceNone, listOf(iceNormal, iceFull))
        }

        iceNormal.setOnClickListener {
            selectedIce = "normal"
            updateIce(iceNormal, listOf(iceNone, iceFull))
        }

        iceFull.setOnClickListener {
            selectedIce = "full"
            updateIce(iceFull, listOf(iceNone, iceNormal))
        }

        updateTotal()
    }
}
