package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel

@Composable
fun SearchBarRow(
    viewModel: ProcessViewModel,
    searchValue: String,
) {

    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(55.dp), horizontalArrangement = Arrangement.Center) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchValue,
            onValueChange = { viewModel.searchProcess(it) },
            placeholder = { Text("Filter", color = Color.Black) },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),

            singleLine = true,
            shape = OutlinedTextFieldDefaults.shape,
            trailingIcon = {

                if(searchValue.isNotEmpty())
                IconButton(
                    onClick = {
                        viewModel.searchProcess("")
                        focusManager.clearFocus()
                    }) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Gray
            ),
        )

        /*
                TextField(
                    value = searchValue,
                    onValueChange = { viewModel.searchProcess(it) },
                    placeholder = { Text("Filter", color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    singleLine = true,
                    shape = RectangleShape,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                )
        */
    }

}
