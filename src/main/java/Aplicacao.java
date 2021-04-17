import static spark.Spark.*;

public class Aplicacao {
	
	private static AlunoService Aluno = new AlunoService();

    public static void main(String[] args)  {
        port(6790);

        post("/aluno", (request, response) -> Aluno.add(request, response));

        get("/aluno/:id", (request, response) -> Aluno.get(request, response));

        get("/aluno/update/:id", (request, response) -> Aluno.update(request, response));

        get("/aluno/delete/:id", (request, response) -> Aluno.remove(request, response));

        get("/aluno", (request, response) -> Aluno.getAll(request, response));
        
        
    }
}