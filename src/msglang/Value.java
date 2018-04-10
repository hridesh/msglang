package msglang;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import msglang.AST.*;
import msglang.Env.ExtendEnv;


public interface Value {
    public String tostring();
    public boolean equals(Value v);

    static class ActorVal extends Thread implements Value { //New in the Actorlang
	private Env _env;
	private List<String> _formals;
	private Exp _body;
	private Evaluator _evaluator;
	private Heap _h;
	private java.util.concurrent.LinkedBlockingDeque<List<Value>> _queue;
	public ActorVal(Env env, List<String> formals, Exp body, Evaluator evaluator, Heap h) {
	    _env = env;
	    _formals = formals;
	    _body = body;
	    _evaluator = evaluator;
	    _h = h;
	    _queue = new java.util.concurrent.LinkedBlockingDeque<List<Value>>();
	    this.start();
	}
	public void run(){
	    while(!_exit()) {
		try {
		    List<Value> actuals = _queue.take();
		    Env receive_env = _env;
		    for (int index = 0; index < _formals.size(); index++)
			receive_env = new ExtendEnv(receive_env, _formals.get(index), actuals.get(index));
		    receive_env = new ExtendEnv(receive_env, "self", this);
		    _body.accept(_evaluator, receive_env, _h);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
	public boolean receive (List<Value> request) throws InterruptedException {
	    if(!_exit()) {
		_queue.put(request);
		return true;
	    }
	    return false;
	}
	public List<String> formals() { return _formals; }
	volatile boolean _exit = false;
	private synchronized boolean _exit() { return _exit; } 
	public synchronized void exit() { _exit = true; }
	public String tostring() {
	    return "actor: " + this;
	}
	public boolean equals(Value v) { 
	    if(v instanceof ActorVal)
		return super.equals(v);
	    return false;
	}
    }
    static class RefVal extends ReentrantLock implements Value { //New in the reflang
	private static final long serialVersionUID = 1L;
	private int _loc = -1; 
	public RefVal(int loc) { _loc = loc; }
	public String tostring() {
	    return "loc:" + this._loc;
	}
	public int loc() { return _loc; }
	public boolean equals(Value v) {
	    if(v instanceof RefVal) {
		return ((RefVal) v)._loc == this._loc;
	    }
	    return false;
	}
    }
    static class FunVal implements Value { //New in the funclang
	private Env _env;
	private List<String> _formals;
	private Exp _body;
	public FunVal(Env env, List<String> formals, Exp body) {
	    _env = env;
	    _formals = formals;
	    _body = body;
	}
	public Env env() { return _env; }
	public List<String> formals() { return _formals; }
	public Exp body() { return _body; }
	public String tostring() { 
	    String result = "(lambda ( ";
	    for(String formal : _formals) 
		result += formal + " ";
	    result += ") ";
	    result += _body.accept(new Printer.Formatter(), _env, null);
	    return result + ")";
	}
	public boolean equals(Value v) { 
	    if(v instanceof FunVal)
		return super.equals(v);
	    return false;
	}
    }
    static class NumVal implements Value {
	private double _val;
	public NumVal(double v) { _val = v; } 
	public double v() { return _val; }
	public String tostring() { 
	    int tmp = (int) _val;
	    if(tmp == _val) return "" + tmp;
	    return "" + _val; 
	}
	public boolean equals(Value v) { 
	    if(v instanceof NumVal) {
		return ((NumVal) v)._val == this._val;
	    }
	    return false;
	}
    }
    static class BoolVal implements Value {
	private boolean _val;
	public BoolVal(boolean v) { _val = v; } 
	public boolean v() { return _val; }
	public String tostring() { if(_val) return "#t"; return "#f"; }
	public boolean equals(Value v) { 
	    if(v instanceof BoolVal) {
		return ((BoolVal) v)._val == this._val;
	    }
	    return false;
	}
    }
    static class StringVal implements Value {
	private java.lang.String _val;
	public StringVal(String v) { _val = v; } 
	public String v() { return _val; }
	public java.lang.String tostring() { return "" + _val; }
	public boolean equals(Value v) { 
	    if(v instanceof StringVal) {
		return ((StringVal) v)._val.equals(_val);
	    }
	    return false;
	}
    }
    static class PairVal implements Value {
	protected Value _fst;
	protected Value _snd;
	public PairVal(Value fst, Value snd) { _fst = fst; _snd = snd; } 
	public Value fst() { return _fst; }
	public Value snd() { return _snd; }
	public java.lang.String tostring() { 
	    if(isList()) return listToString();
	    return "(" + _fst.tostring() + " " + _snd.tostring() + ")"; 
	}
	public boolean isList() {
	    if(_snd instanceof Value.Null) return true;
	    if(_snd instanceof Value.PairVal &&
		    ((Value.PairVal) _snd).isList()) return true;
	    return false;
	}
	private java.lang.String listToString() {
	    String result = "(";
	    result += _fst.tostring();
	    Value next = _snd; 
	    while(!(next instanceof Value.Null)) {
		result += " " + ((PairVal) next)._fst.tostring();
		next = ((PairVal) next)._snd;
	    }
	    return result + ")";
	}
	public boolean equals(Value v) { 
	    if(v instanceof PairVal)
		return ((PairVal) v)._fst.equals(_fst) && ((PairVal) v)._snd.equals(_snd);
	    return false;
	}
    }
    static class Null implements Value {
	public Null() {}
	public String tostring() { return "()"; }
	public boolean equals(Value v) { 
	    if(v instanceof Null) return true;
	    return false;
	}
    }
    static class UnitVal implements Value {
	public static final UnitVal v = new UnitVal();
	public String tostring() { return ""; }
	public boolean equals(Value v) { 
	    if(v instanceof UnitVal) return true; 
	    return false;
	}
    }
    static class DynamicError implements Value { 
	private String message = "Unknown dynamic error.";
	public DynamicError(String message) { this.message = message; }
	public String tostring() { return "" + message; }
	public boolean equals(Value v) { 
	    if(v instanceof UnitVal) return true; 
	    return false;
	}
    }
}
