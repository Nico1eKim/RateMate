package com.example.ratemate.setting

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ratemate.R
import com.example.ratemate.common.CommonTextField
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option(navController: NavHostController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var profSuccessDialog by remember { mutableStateOf(false) }
    var profileimg = "profileImage"

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository()))
    if (uid != null) {
        userViewModel.getUser(uid)
    }
    val Nuser by userViewModel.user.collectAsState(initial = null)

    val scollState = rememberScrollState()

    var purchaseProfileList by remember { mutableStateOf(listOf(R.drawable.profile)) }

    var user by remember { mutableStateOf(Nuser) }

    LaunchedEffect(Nuser) {
        user = Nuser
        Log.d("동기화 확인", user.toString())
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = "Setting",
                onNavigateBack = { },
                false
            )
        }
    ) { paddingValues ->

        user?.let {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                var currentProfile = user!!.profileImage

                var changed by remember { mutableStateOf(false) }

                LaunchedEffect(key1 = user) {
                    currentProfile = user!!.profileImage
                    Log.d("동기화 확인", currentProfile.toString())
                    changed = true
                }


                LaunchedEffect(key1 = Unit, key2 = changed) {
                    val userPurchaseList = user!!.PurchaseList
                    changed = false

                    purchaseProfileList = listOf(R.drawable.profile)

                    for (item in userPurchaseList) {

                        if (item.itemName == "item1") {
                            purchaseProfileList = purchaseProfileList + R.drawable.item1
                        }
                        if (item.itemName == "item2") {
                            purchaseProfileList = purchaseProfileList + R.drawable.item2
                        }
                        if (item.itemName == "item3") {
                            purchaseProfileList = purchaseProfileList + R.drawable.item3
                        }
                        if (item.itemName == "item4") {
                            purchaseProfileList = purchaseProfileList + R.drawable.item4
                        }
                        if (item.itemName == "item5") {
                            purchaseProfileList = purchaseProfileList + R.drawable.item5
                        }

                    }

                    Log.d("동기화 확인", "purchaseProfileList : " + purchaseProfileList.toString())

                }

                Spacer(modifier = Modifier.height(16.dp))
                var imgchange by remember { mutableStateOf(user!!.profileImage) }

                // 프로필 사진 변경 섹션
                SectionTitle(if (purchaseProfileList.isEmpty()) "프로필 사진" else "프로필 사진 변경")
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = imgchange), // 프로필 이미지 리소스
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(20.dp))


                if (purchaseProfileList.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(id = R.color.gray_100))
                            .padding(vertical = 15.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (img in purchaseProfileList) {
                            Image(
                                painter = painterResource(id = img),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        imgchange = img
                                        currentProfile = img
                                    }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            userViewModel.updateUser(uid, mapOf(profileimg to imgchange))
                            profSuccessDialog = true
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorResource(id = R.color.gray_50)
                        ),
                        border = BorderStroke(1.dp, colorResource(id = R.color.gray_400))
                    ) {
                        Text(
                            "이미지 변경",
                            color = colorResource(id = R.color.gray_700),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))


                // 비밀번호 변경 섹션
                SectionTitle("비밀번호 변경")
                Spacer(modifier = Modifier.height(15.dp))
                CommonTextField(
                    value = password,
                    label = "비밀번호",
                    onValueChange = { password = it },
                    placeholder = { Text("입력해주세요.") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CommonTextField(
                    value = confirmPassword,
                    label = "비밀번호 확인",
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("다시 입력해주세요.") }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                            showSuccessDialog = true
                            password = ""
                            confirmPassword = ""
                        } else {
                            showErrorDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(id = R.color.main_blue)
                    ),
                ) {
                    Text(
                        "비밀번호 변경",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))

                // 로그아웃 버튼
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("Start")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(id = R.color.main_blue)
                    ),
                ) {
                    Text("로그아웃", fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white))
                }

                if (showErrorDialog) {
                    AlertDialog(
                        onDismissRequest = { showErrorDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showErrorDialog = false }) {
                                Text("OK")
                            }
                        },
                        title = { Text("오류") },
                        text = { Text("비밀번호가 일치하지 않습니다. 다시 입력해주세요.") }
                    )
                }

                if (showSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = { showSuccessDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showSuccessDialog = false }) {
                                Text("OK")
                            }
                        },
                        title = { Text("성공") },
                        text = { Text("비밀번호가 성공적으로 변경되었습니다.") }
                    )
                }

                if (profSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = { profSuccessDialog = false },
                        confirmButton = {
                            TextButton(onClick = { profSuccessDialog = false }) {
                                Text("OK")
                            }
                        },
                        title = { Text("성공") },
                        text = { Text(" 프로필이 성공적으로 변경되었습니다.") }
                    )
                }
            }
        }

    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
//            .padding(start = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        visualTransformation = PasswordVisualTransformation()
    )
}




