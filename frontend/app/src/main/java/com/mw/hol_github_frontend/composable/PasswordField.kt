package com.mw.hol_github_frontend.composable

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mw.hol_github_frontend.R

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                label,
            )
        },
        trailingIcon = {
            val image = if (!isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

            IconButton(onClick = { onVisibilityChange(!isVisible) }) {
                Icon(
                    imageVector = image, stringResource(R.string.passwordfield_password_visibility)
                )
            }
        },
        singleLine = true,
        visualTransformation = if (!isVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
    )
}