package com.dog

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.feed.LikeViewModel
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.navigation.AppNavigation
import com.dog.ui.theme.PurpleGray400
import com.dog.util.common.DataStoreManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DogApp(dataStoreManager: DataStoreManager, onPermissionDenied: () -> Unit) {


    val permissionsState = rememberMultiplePermissionsState(
        permissions =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )

    val openDialog = remember { mutableStateOf(true) }

    LaunchedEffect(permissionsState) {
        if (permissionsState.allPermissionsGranted) {
            openDialog.value = false

        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    if (permissionsState.allPermissionsGranted) {
        val navController = rememberNavController()
        val userViewModel: UserViewModel = hiltViewModel()
        val locationTrackingViewModel: LocationTrackingViewModel = hiltViewModel()
        val homeViewModel: HomeViewModel = hiltViewModel()
        val isUserLoggedIn = userViewModel.isLogin.collectAsState().value
        val isLoading = userViewModel.isLoading.collectAsState().value
        val gpsIsLoading = locationTrackingViewModel.isLoading.collectAsState().value
        val feedIsLoading = homeViewModel.isLoading.collectAsState().value

        if (isLoading || feedIsLoading || gpsIsLoading) {
            CircularProgressIndicator(color = PurpleGray400)
        } else {
            AppNavigation(navController, userViewModel, dataStoreManager, isUserLoggedIn)
        }
    } else {
        PermissionRequestDialog(permissionsState, openDialog) {
            onPermissionDenied()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestDialog(
    permissionsState: MultiplePermissionsState,
    openDialog: MutableState<Boolean>,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    // 권한 요청 다이얼로그를 표시하는 조건
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                if (!permissionsState.allPermissionsGranted &&
                    permissionsState.permissions.any { !it.status.shouldShowRationale }
                ) {
                    onPermissionDenied()
                }
            },
            title = { Text(text = "권한 요청") },
            text = {
                Text("이 앱은 위치 정보와 알림 권한이 필요합니다. 계속하기 위해서는 권한을 허용해 주세요.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // "다시 묻지 않음"을 선택했을 경우 설정 화면으로 이동합니다.
                        openDialog.value = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("설정으로 이동")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        )
    }
}
