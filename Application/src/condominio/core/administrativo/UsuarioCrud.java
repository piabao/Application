package condominio.core.administrativo;

import java.util.List;

import condominio.server.modelo.PRIVILEGIOS;
import condominio.server.modelo.USUARIO;
import condominio.server.modelo.dao.PrivilegiosJpaController;
import condominio.server.modelo.dao.UsuarioJpaController;
import condominio.server.modelo.dao.exceptions.NonexistentEntityException;

public class UsuarioCrud {
	
	private UsuarioJpaController usuario;
	private PrivilegiosJpaController privilegio;
	
	public UsuarioCrud(){
		usuario = new UsuarioJpaController();
		privilegio = new PrivilegiosJpaController();
	}

	public USUARIO create(USUARIO user) {
		return usuario.create(user);
	}

	public void create(List<PRIVILEGIOS> prvList) {
		privilegio.create(prvList);		
	}

	public USUARIO processaLogin(String user) {
		USUARIO usuarios = usuario.findUSUARIO(user);
		return 	usuarios == null ? new USUARIO() : usuarios;
	}

	public List<PRIVILEGIOS> getPrivilegios(Long id) {		
		return privilegio.findPrivilegiosByUser(id);
	}

	public List<USUARIO> listaUsuarios() {
		return usuario.findUSUARIOEntities();
	}

	public void removerUsuario(USUARIO user) {
		try {
			usuario.destroy(user.getId());
		} catch (NonexistentEntityException e) {
			e.printStackTrace();
		}
	}

}
