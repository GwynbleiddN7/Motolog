package com.gwynn7.motolog.Fragments.Garage

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.gwynn7.motolog.Models.DistanceLog
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.Models.getUpdatedBikeDistance
import com.gwynn7.motolog.Path
import com.gwynn7.motolog.R
import com.gwynn7.motolog.ViewModel.MotorcycleViewModel
import com.gwynn7.motolog.showToast
import com.gwynn7.motolog.yearFromLong
import java.util.Calendar

class MotorcycleAddFragment : Fragment() {
    private lateinit var mMotorcycleViewModel: MotorcycleViewModel
    private val args by navArgs<MotorcycleAddFragmentArgs>()
    private var currentPath: Path = Path.Add
    private var tempBitmap: Bitmap? = null
    private var bShouldRemoveImage: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.motorcycle_add, container, false)
        mMotorcycleViewModel = ViewModelProvider(this)[MotorcycleViewModel::class.java]

        if(args.currentMotorcycle != null) currentPath = Path.Edit

        val imageAdd = view.findViewById<ImageButton>(R.id.ib_bike_image)
        val buttonText = if(currentPath == Path.Add) getString(R.string.add_motorcycle) else getString(R.string.edit_motorcycle)

        if(currentPath == Path.Edit) {
            val bike = args.currentMotorcycle!!
            view.findViewById<EditText>(R.id.et_bike_manufacturer).setText(bike.manufacturer)
            view.findViewById<EditText>(R.id.et_bike_model).setText(bike.model)
            view.findViewById<EditText>(R.id.et_bike_alias).setText(bike.alias)
            view.findViewById<EditText>(R.id.et_bike_year).setText(bike.year.toString())
            view.findViewById<EditText>(R.id.et_bike_startkm).setText(bike.start_km.toString())
            if(bike.image != null) imageAdd.setImageURI(bike.image)
            else imageAdd.setImageResource(R.drawable.add_photo)
        }

        val button = view.findViewById<Button>(R.id.bt_addMotorcycle)
        button.text = buttonText
        button.setOnClickListener {
            insertDataToDatabase(view)
        }


        imageAdd.setOnClickListener{
            uploadImage()
        }

        imageAdd.setOnLongClickListener{
            tempBitmap = null
            bShouldRemoveImage = true
            imageAdd.setImageResource(R.drawable.add_photo)
            true
        }

        setHasOptionsMenu(currentPath == Path.Edit)
        return view
    }

    private fun insertDataToDatabase(view: View) {
        val manufacturer = view.findViewById<EditText>(R.id.et_bike_manufacturer).text.toString()
        val model = view.findViewById<EditText>(R.id.et_bike_model).text.toString()
        val name = view.findViewById<EditText>(R.id.et_bike_alias).text.toString()
        val year = view.findViewById<EditText>(R.id.et_bike_year).text.toString()
        val startKm = view.findViewById<EditText>(R.id.et_bike_startkm).text.toString()

        if(inputCheck(manufacturer, model, year))
        {
            val km = if(startKm.isEmpty()) 0 else startKm.toInt()
            val yearInt = year.toInt()

            if(yearInt > Calendar.getInstance().get(Calendar.YEAR)) {
                showToast(requireContext(), getString(R.string.bike_future))
                return
            }
            if(yearInt < 1900) {
                showToast(requireContext(), getString(R.string.bike_past))
                return
            }
            if(currentPath == Path.Edit)
            {
                val motorcycle = args.currentMotorcycle!!.copy(manufacturer = manufacturer, model = model, alias = name, year = yearInt, start_km = km, personal_km = 0)

                if(motorcycle.km_logs.any { log -> distanceLogsCheck(log, motorcycle.start_km, motorcycle.year) })
                {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setPositiveButton(getString(R.string.delete_logs)){ _,_ ->
                        motorcycle.km_logs = motorcycle.km_logs.filter { log -> !distanceLogsCheck(log, motorcycle.start_km, motorcycle.year) }
                        motorcycle.personal_km = getUpdatedBikeDistance(motorcycle)
                        mMotorcycleViewModel.updateMotorcycle(motorcycle, tempBitmap, bShouldRemoveImage)
                        showToast(requireContext(), getString(R.string.bike_saved))
                        findNavController().navigateUp()
                    }
                    builder.setNegativeButton(getString(R.string.back)){ _,_ -> }
                    builder.setTitle(getString(R.string.log_mismatch))
                    builder.setMessage(getString(R.string.log_mismatch_action))
                    builder.create().show()
                    return
                }
                else
                {
                    motorcycle.personal_km = getUpdatedBikeDistance(motorcycle)
                    mMotorcycleViewModel.updateMotorcycle(motorcycle, tempBitmap, bShouldRemoveImage)
                }
            }
            else
            {
                val motorcycle = Motorcycle(0, manufacturer, model, name, yearInt, km)
                mMotorcycleViewModel.addMotorcycle(motorcycle, tempBitmap)
            }

            showToast(requireContext(), getString(R.string.bike_saved))
            findNavController().navigateUp()
        }
        else showToast(requireContext(), getString(R.string.fill_fields))
    }

    private fun inputCheck(manufacturer: String, model: String, year: String): Boolean
    {
        return manufacturer.isNotEmpty() && model.isNotEmpty() && year.isNotEmpty()
    }

    private fun distanceLogsCheck(log: DistanceLog, startKm: Int, bikeYear: Int): Boolean
    {
        return yearFromLong(log.date) < bikeYear || log.distance < startKm
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete) deleteMotorcycle()
        return super.onContextItemSelected(item)
    }

    private fun deleteMotorcycle()
    {
        val currentBike = args.currentMotorcycle!!
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
            mMotorcycleViewModel.deleteMotorcycle(currentBike)
            showToast(requireContext(),getString(R.string.bike_delete))
            findNavController().navigateUp()
        }
        builder.setNegativeButton(getString(R.string.no)){ _,_ -> }
        builder.setTitle("${getString(R.string.delete)} ${currentBike.manufacturer} ${currentBike.model}?")
        builder.setMessage(getString(R.string.delete_bike_question))
        builder.create().show()
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful && result.uriContent != null) {
            val uriContent = result.uriContent!!

            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uriContent))
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uriContent)
            }
            requireView().findViewById<ImageButton>(R.id.ib_bike_image).setImageBitmap(bitmap)
            tempBitmap = bitmap
        }
    }

    private fun uploadImage() {
        val options = CropImageContractOptions(null, CropImageOptions(imageSourceIncludeGallery = true, imageSourceIncludeCamera = false))
        cropImage.launch(options)
    }
}