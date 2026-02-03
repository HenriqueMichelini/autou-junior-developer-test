import { useEffect, useRef, useState } from "react";

type ScrollableContainerProps = {
  children: React.ReactNode;
};

function ScrollableContainer({ children }: ScrollableContainerProps) {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [showTopShadow, setShowTopShadow] = useState(false);
  const [showBottomShadow, setShowBottomShadow] = useState(false);

  const handleScroll = () => {
    if (!scrollRef.current) return;

    const { scrollTop, scrollHeight, clientHeight } = scrollRef.current;

    setShowTopShadow(scrollTop > 0);

    setShowBottomShadow(scrollTop + clientHeight < scrollHeight - 1);
  };

  useEffect(() => {
    handleScroll();

    const element = scrollRef.current;
    element?.addEventListener("scroll", handleScroll);

    const timer = setTimeout(handleScroll, 100);

    return () => {
      element?.removeEventListener("scroll", handleScroll);
      clearTimeout(timer);
    };
  }, [children]);

  return (
    <div className="relative flex-1 overflow-hidden">
      <div
        className={`
          absolute top-0 left-0 right-0 h-16 pointer-events-none z-10
          bg-gradient-to-b from-white to-transparent
          transition-opacity duration-200
          ${showTopShadow ? "opacity-100" : "opacity-0"}
        `}
      />

      <div ref={scrollRef} className="h-full overflow-y-auto">
        {children}
      </div>

      <div
        className={`
          absolute bottom-0 left-0 right-0 h-16 pointer-events-none z-10
          bg-gradient-to-t from-white to-transparent
          transition-opacity duration-200
          ${showBottomShadow ? "opacity-100" : "opacity-0"}
        `}
      />
    </div>
  );
}

export default ScrollableContainer;
