export default function Logo({ large, sidebar }) {
  return (
    <span className="inline-flex rounded-8 bg-logo-fundo px-2 py-1">
      <img
        src="/logo.png"
        alt="JR Barbershop"
        className={`block h-auto object-contain ${large ? 'w-56' : sidebar ? 'w-52' : 'w-32'}`}
      />
    </span>
  )
}
