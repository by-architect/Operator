package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.R
import com.byarchitect.operator.common.constant.ProcessScreenSearchScrollManager
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel

@Composable
fun SearchBarRow(
    viewModel: ProcessViewModel,
    mainScreenScrollManager: ProcessScreenSearchScrollManager,
    searchValue: String,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
/*
                viewModel.closeSearchBar()
                focusManager.clearFocus()
                coroutineScope.launch {
                    scrollState.animateScrollTo(
                        scrollState.maxValue,
                        animationSpec = AnimationSpecs.containerAnimation
                    )

                }
*/
            }) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Settings,
                tint = Color.Gray,
                contentDescription = stringResource(R.string.settings),
            )
        }
        OutlinedTextField(
            modifier = Modifier.height(56.dp),
            value = searchValue,
            onValueChange = { viewModel.searchProcess(it) },
            placeholder = { Text(text = stringResource(R.string.filter), color = Color.Black) },
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            singleLine = true,
            shape = RoundedCornerShape(32.dp),
            trailingIcon = {
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
        IconButton(
            onClick = {
                mainScreenScrollManager.closeSearchBar()

            }) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Clear,
                tint = Color.Gray,
                contentDescription = stringResource(R.string.exit),
            )
        }
    }


}
