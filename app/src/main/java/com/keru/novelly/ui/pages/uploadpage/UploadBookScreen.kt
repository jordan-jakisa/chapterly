package com.keru.novelly.ui.pages.uploadpage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.keru.novelly.data.data_source.local.models.genreDetailsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadBookScreen(
    navController: NavController,
    vm: UploadBookViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {

        var isExpanded by rememberSaveable { mutableStateOf(false) }
        val scrollState = rememberScrollState()
        var title by rememberSaveable { mutableStateOf("") }
        var description by rememberSaveable { mutableStateOf("") }
        var author by rememberSaveable { mutableStateOf("") }
        var genre by rememberSaveable { mutableStateOf("") }
        var imageUri by rememberSaveable { mutableStateOf(Uri.EMPTY) }
        var bookRating by rememberSaveable { mutableFloatStateOf(0f) }

        val pickImage =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    imageUri = it
                }
            }


        TopAppBar(
            title = {
                Text(text = "Upload book")
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "")
                }
            })
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "* you can now upload your own books to the Chapterly catalogue this enables you and the entire world to be able to read and gain access to these books. Note: The books you upload are publicly available to the public. Thank you for the initiative, please fill in all the information below.",
                fontSize = 12.sp,
                modifier = Modifier.alpha(.75f)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Book title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Book description") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(text = "Book author") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bookRating.toString(),
                onValueChange = { bookRating = it.toFloat() },
                label = { Text(text = "Book rating") },
                placeholder = { Text(text = "e.g 4.5 ") },
                modifier = Modifier.fillMaxWidth(),
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Book genre") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {
                        isExpanded = false
                    }
                ) {
                    genreDetailsList.map { it.name }.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                genre = selectionOption
                                isExpanded = false
                            })
                    }
                }
            }
            ElevatedButton(onClick = {
                pickImage.launch("image/*")
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Click to select a cover image for the book")
            }
            AnimatedVisibility(
                visible = imageUri.toString().isNotEmpty(),
                enter = slideInHorizontally { it },
                exit = slideOutVertically { it }) {
                Column {
                    Text(text = "Selected image")
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "",
                        modifier = Modifier
                            .size(128.dp)
                            .clickable { pickImage.launch("image/*") })
                }
            }

            Button(
                onClick = {
                    vm.uploadBook(title, description, author, genre, imageUri, bookRating)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotEmpty() && description.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty() && imageUri.toString()
                    .isNotEmpty()
            ) {
                Text(text = "Save")
            }
        }
    }
}