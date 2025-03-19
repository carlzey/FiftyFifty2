package sick.sick.fiftyfifty2

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import sick.sick.fiftyfifty2.history.HistoryDatabase
import sick.sick.fiftyfifty2.vote.Question
import sick.sick.fiftyfifty2.vote.QuestionViewModel
import sick.sick.fiftyfifty2.vote.QuestionViewModelFactory


@Composable
fun SocialWindow() {


    val context = LocalContext.current

    val database = HistoryDatabase.getDatabase(context)

    val questionViewModel: QuestionViewModel = viewModel(
        factory = QuestionViewModelFactory(database.questionDao(), database.voteDao())
    )
    val questions = questionViewModel.questions.collectAsState(initial = emptyList()).value
    //val questionss: Flow<List<Question>> = questionViewModel.questions



    //debug
    Log.d("SocialWindow", "Questions: $questions")


    //val questions = questionViewModel.questions.collectAsState(initial = emptyList()).value

            //QuestionViewModelFactory(database.questionDao(), database.voteDao())
    //)



    Column {
        Text("Social Window", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(50.dp))
        Text("$questions", fontSize = 24.sp)


        questions.forEach { question ->
            Text(question.question, fontSize = 24.sp)
            //Text(question.options.joinToString(", "), fontSize = 24.sp)
            question.id //debugd
            var quest = question.id
        //}
            question.options.forEach { option ->
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { questionViewModel.vote(question.id.toInt(), option) }) {
                    Text(text = option)
                }

                    //Text(question.question, fontSize = 24.sp)
                    //Text(question.options.joinToString(", "), fontSize = 24.sp)

            }
        } }
        //visa alla val i en lista
//        LazyColumn {
//            items(questions.size) { question -> // question är en val
//                Card(
//                    modifier = Modifier.padding(8.dp)
//                        .fillMaxWidth(),
//                    ///elevation =  elevation????????
//                ) // slut på card todo
//                {
//                    Column(
//                        modifier = Modifier.padding(16.dp)) {
//                        Text(text = question, fontSize = FontWeight.Bold) // Bold text för fråga
//                        Spacer(modifier = Modifier.height(8.dp))
//
//
//                        // Knapp för att att rösta på varje alternativ
//                        question.options.forEach {  -> options
//                            Button(onClick = { questionViewModel.vote(questions.id, questions.option) }) {
//                                Text(text = questions.option)
//                            }
//                            Spacer(modifier = Modifier.height(8.dp))
//                        }
//
//                    }
        //  Text(questions.question, modifier = Modifier.padding(16.dp))
//                }
//            }
        }



