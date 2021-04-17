package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import spark.Request;
import spark.Response;

public class AlunoService {

	private AlunoDAO AlunoDAO;

	public AlunoService() {
		try {

			AlunoDAO = new AlunoDAO();
			AlunoDAO.conectar();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public Object add(Request request, Response response) {
		String nome = request.queryParams("nome");
		String curso = request.queryParams("curso");
		String sexo = request.queryParams("sexo");
		int idade = Integer.parseInt(request.queryParams("idade"));

		int id = AlunoDAO.getMaxId() + 1;

		Aluno Aluno = new Aluno(id, nome, curso, sexo, idade);

		AlunoDAO.inserirAluno(Aluno);

		response.status(201); // 201 Created
		return id;
	}

	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));

		Aluno aluno = (Aluno) AlunoDAO.procurar_aluno(id);

		if (aluno != null) {
			response.header("Content-Type", "application/xml");
			response.header("Content-Encoding", "UTF-8");

			return "<aluno>\n" + "\t<id> " + aluno.getId() + "</id>\n" + "\t<Nome> " + aluno.getNome() + "</Nome>\n"
					+ "\t<Curso> " + aluno.getCurso() + "</Curso>\n" + "\t<Sexo> " + aluno.getSexo() + "</Sexo>\n"
					+ "\t<Idade> " + aluno.getIdade() + "</Idade>\n" + "</aluno>\n";
		} else {
			response.status(404); // 404 Not found
			return "Produto " + id + " n�o encontrado.";
		}

	}

	public Object update(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));

		Aluno aluno = (Aluno) AlunoDAO.procurar_aluno(id);

		if (aluno != null) {
			aluno.setNome(request.queryParams("nome"));
			aluno.setCurso(request.queryParams("curso"));
			aluno.setSexo(request.queryParams("sexo").charAt(0));
			aluno.setIdade(Integer.parseInt(request.queryParams("idade")));

			AlunoDAO.atualizarAluno(aluno);

			return id;
		} else {
			response.status(404); // 404 Not found
			return "Bem de consumo n�o encontrado.";
		}

	}

	public Object remove(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));

		Aluno aluno = (Aluno) AlunoDAO.procurar_aluno(id);

		if (aluno != null) {

			AlunoDAO.excluirAluno(aluno);

			response.status(200); // success
			return id;
		} else {
			response.status(404); // 404 Not found
			return "Bem de consumo n�o encontrado.";
		}
	}

	public Object getAll(Request request, Response response) {
		StringBuffer returnValue = new StringBuffer("<aluno type=\"array\">");
		for (Aluno Aluno : AlunoDAO.getAll()) {
			Aluno aluno = (Aluno) aluno;
			returnValue.append("\n<aluno>\n" + "\t<id> " + aluno.getId() + "</id>\n" + "\t<Nome> " + aluno.getNome()
					+ "</Nome>\n" + "\t<Curso> " + aluno.getCurso() + "</Curso>\n" + "\t<Sexo> " + aluno.getSexo()
					+ "</Sexo>\n" + "\t<Idade> " + aluno.getIdade() + "</Idade>\n" + "</aluno>\n");
		}
		returnValue.append("</aluno>");
		response.header("Content-Type", "application/xml");
		response.header("Content-Encoding", "UTF-8");
		return returnValue.toString();

	}

}
