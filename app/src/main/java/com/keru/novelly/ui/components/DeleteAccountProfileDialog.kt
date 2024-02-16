package com.keru.novelly.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DeleteAccountDialog(
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    ElevatedCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "This action cannot be undone. Are you sure you want to continue.")
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onCancel() }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(onClick = { onClick() }) {
                    Text(text = "Continue")
                }
            }

        }
    }
}

@Preview
@Composable
fun DeleteAccountDialogPrev() {
    DeleteAccountDialog(onCancel = {}, onClick = {})

}