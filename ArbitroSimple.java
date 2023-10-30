package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;

/**
 * Arbitro simple.
 * 
 * Contiene las funciones específicas para partidas simples.
 * 
 * @author <a href="mailto:btx1002@alu.ubu.es">Beatrice Izabel Toth</a>
 * @author <a href="mailto:lma1006@alu.ubu.es">Luis Martín Asenjo</a>
 * 
 * @since Java 16
 * @version 4.0
 * @see juego.control.ArbitroAbstracto
 */
public class ArbitroSimple extends ArbitroAbstracto {
	
	/**
	 * Constructor de arbitro simple.
	 * 
	 * Inicialmente se inicializan los arrays de torres y el tablero.
	 * Después se inicia la jugada a 0.
	 * 
	 * @param tablero tablero de kamisado
	 */
	public ArbitroSimple(Tablero tablero) {
		super(tablero);
		
		jugada = 0;
	}
	
	/**
	 * Consulta el ganador de la ronda.
	 * 
	 * <p>
	 * Un turno puede ganar llegando al final del tablero o por
	 * bloqueo mutuo.
	 * </p>
	 * 
	 * @return turno ganador de la ronda
	 */
	@Override
	public Turno consultarGanadorRonda() {
		return consultarGanadorPartida();
	}

	/**
	 * Comprueba si la ronda actual está acabada.
	 * 
	 * @return true en caso afirmativo o false en caso contrario
	 */
	@Override
	public boolean estaAcabadaRonda() {
		return estaAcabadaPartida();
	}
	
	/**
	 * Realiza un empujón sumo.
	 * 
	 * En partidas simples esta operación no está soportada.
	 * 
	 * @param origen celda origen
	 */
	@Override
	public void empujarSumo(Celda origen) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * Reinicia la ronda.
	 * 
	 * En partidas simples esta operación no está soportada.
	 */
	@Override
	public void reiniciarRonda() {
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * Comprueba si el empujón sumo es legal.
	 * 
	 * En partidas simples esta operación no está soportada.
	 * 
	 * @param origen celda origen
	 * @return true si es legal o false en caso contrario
	 * @throws CoordenadasIncorrectasException si la celda origen o celdas del empujón están fuera del tablero
	 */
	@Override
	public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {
		return false;
	}

	/**
	 * Comprueba si la partida ha finalizado.
	 * 
	 * En partidas simples es lo mismo que consultar si la ronda ha finalizado.
	 * 
	 * @return true si ha finalizado o false en caso contrario
	 */
	@Override
	public boolean estaAcabadaPartida() {
		if (jugada < 2) {
			return false;
		} else if (consultarGanadorPartida() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Consulta el ganador de la partida.
	 * 
	 * En partidas simples es lo mismo que consultar el ganador de la ronda.
	 * 
	 * @return turno ganador de la partida
	 */
	@Override
	public Turno consultarGanadorPartida() {
		Turno turno = obtenerTurno();
			
			if (jugada >= 2) {
				if (BlancoAlcanzaFinal()){
					return Turno.BLANCO;
				} else if (NegroAlcanzaFinal()){
					return Turno.NEGRO;
				} else if (hayBloqueoMutuo()){
					return turno;
				}
			}
			return null;	
		}
}