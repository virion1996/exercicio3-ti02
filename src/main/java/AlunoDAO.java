//package com.ti2cc;

import java.io.Console;
import java.sql.*;

public class AlunoDAO {
	private Connection conexao;
	private List<Aluno> alunos;
	private int max_ID = 0;
	
	public AlunoDAO() {
		conexao = null;
	}

	public int getMaxId() {
		return max_ID;
	}
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "localhost";
		String mydatabase = "Aluno";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "postgres";
		String password = "5823656";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexao efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NAO efetuada com o postgres -- Driver nao encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NAO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
	public boolean inserirAluno(Aluno Aluno) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO Aluno (ID, Nome, Curso, Sexo, Idade) "
					       + "VALUES ("+Aluno.getID()+ ", '" + Aluno.getNome() + "', '"  
					       + Aluno.getCurso() + "', '" + Aluno.getSexo() + "' , '" + Aluno.getIdade() + "');");
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean atualizarAluno(Aluno Aluno) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE Aluno SET Nome = '" + Aluno.getNome() + "', Curso = '"  
				       + Aluno.getCurso() + "', Sexo = '" + Aluno.getSexo() + "', Idade = '" + Aluno.getIdade() + "'"
					   + " WHERE ID = " + Aluno.getID();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean excluirAluno(String ID) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM Aluno WHERE ID = " + ID);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	public Aluno procurar_aluno(int ID) {
		Aluno Aluno = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM Aluno WHERE ID = " + ID);		
	         if(rs.next())
	                Aluno = new Aluno(rs.getString("ID"), rs.getString("Nome"), rs.getString("Curso"), rs.getString("Sexo").charAt(0), rs.getInt("Idade"));
			 
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Aluno;
	}
	
	
	public Aluno[] getAlunos() {
		Aluno[] Alunos = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM Aluno");		
	         if(rs.next()){
	             rs.last();
	             Alunos = new Aluno[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                Alunos[i] = new Aluno(rs.getString("ID"), rs.getString("Nome"), 
	                		                  rs.getString("Curso"), rs.getString("Sexo").charAt(0), rs.getInt("Idade"));
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Alunos;
	}

}