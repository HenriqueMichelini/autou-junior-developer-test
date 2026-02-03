import { Paperclip, Send } from "lucide-react";

function TextField() {
  return (
    <div
      className="
        flex items-center gap-2
        w-full h-12
        bg-[#606060]
        rounded-lg
        px-2
        focus-within:ring-2
        focus-within:ring-white/30
      "
    >
      {/* Text input */}
      <input
        type="text"
        placeholder="Type something…"
        className="
          flex-1
          h-full
          bg-transparent
          text-white
          placeholder-white/60
          outline-none
          px-2
        "
      />

      {/* File upload */}
      <label
        className="
          flex items-center justify-center
          w-8 h-8
          rounded-md
          text-white
          hover:bg-white/20
          transition
          cursor-pointer
        "
      >
        <Paperclip size={18} />
        <input type="file" className="hidden" />
      </label>

      {/* Submit */}
      <button
        type="submit"
        className="
          flex items-center justify-center
          w-8 h-8
          rounded-md
          bg-white
          text-black
          hover:bg-white/90
          transition
        "
      >
        <Send size={18} />
      </button>
    </div>
  );
}

export default TextField;
