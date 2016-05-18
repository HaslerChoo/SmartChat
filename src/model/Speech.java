package model;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import error.Log;
import error.LogType;

public class Speech {
	private  static Voice voice;
	
	private final String KEY="mbrola.base";
	private final String PATH="mbrola";
	private final String VOICE_LANG="mbrola_us1";
	
	
	public Speech()
	{

		System.setProperty(KEY,PATH);
		VoiceManager vm = VoiceManager.getInstance();
		voice = vm.getVoice(VOICE_LANG);
		if (voice == null)
		{
			Log.addLog(LogType.ERROR, "Erro a iniciar o speecher");
		}
		if (voice != null) {
			voice.allocate();
		}
	}
	
	public static void speak(String msg)
	{
		if (voice == null) {
			Log.addLog(LogType.ERROR, "Erro de speecher");
			return;
		}
		voice.speak(msg);
//		voice.deallocate();
	}
	

}