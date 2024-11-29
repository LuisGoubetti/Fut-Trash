import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class JogoUtils {
    // Método para salvar o progresso em um arquivo JSON
    public static void salvarProgresso(ProgressoDoJogo progresso, String caminhoArquivo) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(progresso, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar o progresso do arquivo JSON
    public static ProgressoDoJogo carregarProgresso(String caminhoArquivo) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            return gson.fromJson(reader, ProgressoDoJogo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
